/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 *
 * Contributor(s):
 *
 * Portions Copyrighted 2013 Sun Microsystems, Inc.
 */
package org.netbeans.modules.php.wordpress.ui.actions;

import org.netbeans.api.options.OptionsDisplayer;
import org.netbeans.modules.php.api.phpmodule.PhpModule;
import org.netbeans.modules.php.api.util.StringUtils;
import org.netbeans.modules.php.spi.framework.actions.RunCommandAction;
import org.netbeans.modules.php.wordpress.WordPressPhpProvider;
import org.netbeans.modules.php.wordpress.commands.WordPressCli;
import org.netbeans.modules.php.wordpress.ui.options.WordPressOptions;
import org.netbeans.modules.php.wordpress.util.WPUtils;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.NbBundle;

/**
 *
 * @author junichi11
 */
public class WordPressRunCommandAction extends RunCommandAction {

    private static final WordPressRunCommandAction INSTANCE = new WordPressRunCommandAction();
    private static final long serialVersionUID = 158739462398606689L;

    private WordPressRunCommandAction() {
    }

    public static WordPressRunCommandAction getInstance() {
        return INSTANCE;
    }

    @Override
    public void actionPerformed(PhpModule phpModule) {
        if (!WPUtils.isWP(phpModule)) {
            return;
        }
        String wpCliPath = WordPressOptions.getInstance().getWpCliPath();
        if (StringUtils.isEmpty(wpCliPath)) {
            openOptionsPanel(Bundle.WordPressRunCommandAction_message_no_wp_cli());
            return;
        }
        String error = WordPressCli.validate(wpCliPath);
        if (error != null) {
            openOptionsPanel(Bundle.WordPressRunCommandAction_message_invalid_wp_cli());
            return;
        }
        WordPressPhpProvider.getInstance().getFrameworkCommandSupport(phpModule).openPanel();
    }

    @NbBundle.Messages({
        "# {0} - action name",
        "WordPressRunCommandAction.name=WordPress: {0}"
    })
    @Override
    protected String getFullName() {
        return Bundle.WordPressRunCommandAction_name(getPureName());
    }

    @NbBundle.Messages({
        "WordPressRunCommandAction.message.no.wp-cli=Please set wp-cli path.",
        "WordPressRunCommandAction.message.invalid.wp-cli=Please set valid wp-cli path."
    })
    private void openOptionsPanel(String errorMessage) {
        NotifyDescriptor.Message message = new NotifyDescriptor.Message(
                errorMessage,
                NotifyDescriptor.ERROR_MESSAGE
        );
        DialogDisplayer.getDefault().notify(message);
        OptionsDisplayer.getDefault().open(WordPressOptions.getOptionsPath());
    }

}

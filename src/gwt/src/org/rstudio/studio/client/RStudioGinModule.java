/*
 * RStudioGinModule.java
 *
 * Copyright (C) 2009-12 by RStudio, Inc.
 *
 * This program is licensed to you under the terms of version 3 of the
 * GNU Affero General Public License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * AGPL (http://www.gnu.org/licenses/agpl-3.0.txt) for more details.
 *
 */
package org.rstudio.studio.client;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import org.rstudio.studio.client.application.ApplicationQuit;
import org.rstudio.studio.client.application.ApplicationView;
import org.rstudio.studio.client.application.events.EventBus;
import org.rstudio.studio.client.application.model.ApplicationServerOperations;
import org.rstudio.studio.client.application.ui.ApplicationWindow;
import org.rstudio.studio.client.common.ConsoleDispatcher;
import org.rstudio.studio.client.common.DefaultGlobalDisplay;
import org.rstudio.studio.client.common.GlobalDisplay;
import org.rstudio.studio.client.common.codetools.CodeToolsServerOperations;
import org.rstudio.studio.client.common.compilepdf.model.CompilePdfServerOperations;
import org.rstudio.studio.client.common.console.ConsoleProcess.ConsoleProcessFactory;
import org.rstudio.studio.client.common.crypto.CryptoServerOperations;
import org.rstudio.studio.client.common.filetypes.FileTypeCommands;
import org.rstudio.studio.client.common.latex.LatexProgramRegistry;
import org.rstudio.studio.client.common.mirrors.DefaultCRANMirror;
import org.rstudio.studio.client.common.mirrors.model.MirrorsServerOperations;
import org.rstudio.studio.client.common.rnw.RnwWeaveRegistry;
import org.rstudio.studio.client.common.rpubs.model.RPubsServerOperations;
import org.rstudio.studio.client.common.satellite.Satellite;
import org.rstudio.studio.client.common.satellite.SatelliteManager;
import org.rstudio.studio.client.common.spelling.model.SpellingServerOperations;
import org.rstudio.studio.client.common.synctex.Synctex;
import org.rstudio.studio.client.common.synctex.model.SynctexServerOperations;
import org.rstudio.studio.client.common.vcs.AskPassManager;
import org.rstudio.studio.client.common.vcs.GitServerOperations;
import org.rstudio.studio.client.common.vcs.SVNServerOperations;
import org.rstudio.studio.client.common.vcs.VCSServerOperations;
import org.rstudio.studio.client.common.vcs.ignore.Ignore;
import org.rstudio.studio.client.common.vcs.ignore.IgnoreDialog;
import org.rstudio.studio.client.htmlpreview.HTMLPreview;
import org.rstudio.studio.client.htmlpreview.HTMLPreviewPresenter;
import org.rstudio.studio.client.htmlpreview.model.HTMLPreviewServerOperations;
import org.rstudio.studio.client.htmlpreview.ui.HTMLPreviewApplicationView;
import org.rstudio.studio.client.htmlpreview.ui.HTMLPreviewApplicationWindow;
import org.rstudio.studio.client.htmlpreview.ui.HTMLPreviewPanel;
import org.rstudio.studio.client.pdfviewer.PDFViewer;
import org.rstudio.studio.client.pdfviewer.PDFViewerPresenter;
import org.rstudio.studio.client.pdfviewer.ui.PDFViewerApplicationView;
import org.rstudio.studio.client.pdfviewer.ui.PDFViewerApplicationWindow;
import org.rstudio.studio.client.pdfviewer.ui.PDFViewerPanel;
import org.rstudio.studio.client.projects.Projects;
import org.rstudio.studio.client.projects.model.ProjectsServerOperations;
import org.rstudio.studio.client.server.Server;
import org.rstudio.studio.client.server.remote.RemoteServer;
import org.rstudio.studio.client.vcs.VCSApplicationView;
import org.rstudio.studio.client.vcs.ui.VCSApplicationWindow;
import org.rstudio.studio.client.workbench.ClientStateUpdater;
import org.rstudio.studio.client.workbench.WorkbenchContext;
import org.rstudio.studio.client.workbench.WorkbenchListManager;
import org.rstudio.studio.client.workbench.WorkbenchMainView;
import org.rstudio.studio.client.workbench.codesearch.CodeSearch;
import org.rstudio.studio.client.workbench.codesearch.model.CodeSearchServerOperations;
import org.rstudio.studio.client.workbench.codesearch.ui.CodeSearchWidget;
import org.rstudio.studio.client.workbench.commands.Commands;
import org.rstudio.studio.client.workbench.model.Session;
import org.rstudio.studio.client.workbench.model.WorkbenchListsServerOperations;
import org.rstudio.studio.client.workbench.model.WorkbenchServerOperations;
import org.rstudio.studio.client.workbench.prefs.model.PrefsServerOperations;
import org.rstudio.studio.client.workbench.ui.WorkbenchScreen;
import org.rstudio.studio.client.workbench.ui.WorkbenchTab;
import org.rstudio.studio.client.workbench.views.buildtools.BuildPresenter;
import org.rstudio.studio.client.workbench.views.buildtools.BuildPane;
import org.rstudio.studio.client.workbench.views.buildtools.BuildTab;
import org.rstudio.studio.client.workbench.views.buildtools.model.BuildServerOperations;
import org.rstudio.studio.client.workbench.views.choosefile.ChooseFile;
import org.rstudio.studio.client.workbench.views.choosefile.model.ChooseFileServerOperations;
import org.rstudio.studio.client.workbench.views.console.ConsolePane;
import org.rstudio.studio.client.workbench.views.console.model.ConsoleServerOperations;
import org.rstudio.studio.client.workbench.views.console.shell.Shell;
import org.rstudio.studio.client.workbench.views.console.shell.ShellPane;
import org.rstudio.studio.client.workbench.views.data.Data;
import org.rstudio.studio.client.workbench.views.data.DataPane;
import org.rstudio.studio.client.workbench.views.data.DataTab;
import org.rstudio.studio.client.workbench.views.data.model.DataServerOperations;
import org.rstudio.studio.client.workbench.views.edit.Edit;
import org.rstudio.studio.client.workbench.views.edit.model.EditServerOperations;
import org.rstudio.studio.client.workbench.views.edit.ui.EditView;
import org.rstudio.studio.client.workbench.views.files.Files;
import org.rstudio.studio.client.workbench.views.files.FilesPane;
import org.rstudio.studio.client.workbench.views.files.FilesTab;
import org.rstudio.studio.client.workbench.views.files.model.FilesServerOperations;
import org.rstudio.studio.client.workbench.views.output.find.FindOutputPane;
import org.rstudio.studio.client.workbench.views.output.find.FindOutputPresenter;
import org.rstudio.studio.client.workbench.views.output.find.FindOutputTab;
import org.rstudio.studio.client.workbench.views.output.find.model.FindInFilesServerOperations;
import org.rstudio.studio.client.workbench.views.help.Help;
import org.rstudio.studio.client.workbench.views.help.HelpPane;
import org.rstudio.studio.client.workbench.views.help.HelpTab;
import org.rstudio.studio.client.workbench.views.help.model.HelpServerOperations;
import org.rstudio.studio.client.workbench.views.help.search.HelpSearch;
import org.rstudio.studio.client.workbench.views.help.search.HelpSearchWidget;
import org.rstudio.studio.client.workbench.views.history.History;
import org.rstudio.studio.client.workbench.views.history.HistoryTab;
import org.rstudio.studio.client.workbench.views.history.model.HistoryServerOperations;
import org.rstudio.studio.client.workbench.views.history.view.HistoryPane;
import org.rstudio.studio.client.workbench.views.output.compilepdf.CompilePdfOutputPane;
import org.rstudio.studio.client.workbench.views.output.compilepdf.CompilePdfOutputPresenter;
import org.rstudio.studio.client.workbench.views.output.compilepdf.CompilePdfOutputTab;
import org.rstudio.studio.client.workbench.views.packages.Packages;
import org.rstudio.studio.client.workbench.views.packages.PackagesPane;
import org.rstudio.studio.client.workbench.views.packages.PackagesTab;
import org.rstudio.studio.client.workbench.views.packages.model.PackagesServerOperations;
import org.rstudio.studio.client.workbench.views.plots.Plots;
import org.rstudio.studio.client.workbench.views.plots.PlotsPane;
import org.rstudio.studio.client.workbench.views.plots.PlotsTab;
import org.rstudio.studio.client.workbench.views.plots.model.PlotsServerOperations;
import org.rstudio.studio.client.workbench.views.source.Source;
import org.rstudio.studio.client.workbench.views.source.SourcePane;
import org.rstudio.studio.client.workbench.views.source.editors.EditingTargetSource;
import org.rstudio.studio.client.workbench.views.source.editors.text.AceEditor;
import org.rstudio.studio.client.workbench.views.source.editors.text.DocDisplay;
import org.rstudio.studio.client.workbench.views.source.model.SourceServerOperations;
import org.rstudio.studio.client.workbench.views.source.model.TexServerOperations;
import org.rstudio.studio.client.workbench.views.vcs.VCSTab;
import org.rstudio.studio.client.workbench.views.vcs.common.diff.LineTablePresenter;
import org.rstudio.studio.client.workbench.views.vcs.common.diff.LineTableView;
import org.rstudio.studio.client.workbench.views.vcs.dialog.HistoryPanel;
import org.rstudio.studio.client.workbench.views.vcs.dialog.HistoryPresenter;
import org.rstudio.studio.client.workbench.views.vcs.dialog.ReviewPresenter;
import org.rstudio.studio.client.workbench.views.vcs.dialog.ReviewPresenterImpl;
import org.rstudio.studio.client.workbench.views.vcs.git.GitPane;
import org.rstudio.studio.client.workbench.views.vcs.git.GitPresenter;
import org.rstudio.studio.client.workbench.views.vcs.git.dialog.GitReviewPanel;
import org.rstudio.studio.client.workbench.views.vcs.git.dialog.GitReviewPresenter;
import org.rstudio.studio.client.workbench.views.vcs.svn.SVNPane;
import org.rstudio.studio.client.workbench.views.vcs.svn.SVNPresenter;
import org.rstudio.studio.client.workbench.views.vcs.svn.dialog.SVNReviewPanel;
import org.rstudio.studio.client.workbench.views.vcs.svn.dialog.SVNReviewPresenter;
import org.rstudio.studio.client.workbench.views.workspace.Workspace;
import org.rstudio.studio.client.workbench.views.workspace.WorkspacePane;
import org.rstudio.studio.client.workbench.views.workspace.WorkspaceTab;
import org.rstudio.studio.client.workbench.views.workspace.model.WorkspaceServerOperations;

public class RStudioGinModule extends AbstractGinModule
{
   @Override
   protected void configure()
   {
      bind(EventBus.class).in(Singleton.class) ;
      bind(Session.class).in(Singleton.class) ;
      bind(Projects.class).in(Singleton.class);
      bind(Satellite.class).in(Singleton.class);
      bind(SatelliteManager.class).in(Singleton.class);
      bind(AskPassManager.class).in(Singleton.class);
      bind(WorkbenchContext.class).asEagerSingleton();
      bind(WorkbenchListManager.class).asEagerSingleton();
      bind(ApplicationQuit.class).asEagerSingleton();
      bind(ClientStateUpdater.class).asEagerSingleton();
      bind(ConsoleProcessFactory.class).asEagerSingleton();
      bind(RnwWeaveRegistry.class).asEagerSingleton();
      bind(LatexProgramRegistry.class).asEagerSingleton();
      bind(Commands.class).in(Singleton.class);
      bind(DefaultCRANMirror.class).in(Singleton.class);
      bind(ChooseFile.class).in(Singleton.class);
      bind(ConsoleDispatcher.class).in(Singleton.class);
      bind(FileTypeCommands.class).in(Singleton.class);
      bind(Synctex.class).in(Singleton.class);
      bind(PDFViewer.class).in(Singleton.class);
      bind(HTMLPreview.class).in(Singleton.class);      

      bind(ApplicationView.class).to(ApplicationWindow.class)
            .in(Singleton.class) ;
      
      bind(VCSApplicationView.class).to(VCSApplicationWindow.class)
            .in(Singleton.class);
      bind(ReviewPresenter.class).to(ReviewPresenterImpl.class);
      
      bind(PDFViewerApplicationView.class).to(PDFViewerApplicationWindow.class);
      bind(HTMLPreviewApplicationView.class).to(HTMLPreviewApplicationWindow.class);
      
      bind(Server.class).to(RemoteServer.class) ;
      bind(WorkbenchServerOperations.class).to(RemoteServer.class) ;

      bind(EditingTargetSource.class).to(EditingTargetSource.Impl.class);

      // Bind workbench views
      bindPane("Console", ConsolePane.class); // eager loaded
      bind(Source.Display.class).to(SourcePane.class);
      bind(History.Display.class).to(HistoryPane.class);
      bind(Workspace.Display.class).to(WorkspacePane.class);
      bind(Data.Display.class).to(DataPane.class);
      bind(Files.Display.class).to(FilesPane.class);
      bind(Plots.Display.class).to(PlotsPane.class);
      bind(Packages.Display.class).to(PackagesPane.class);
      bind(Help.Display.class).to(HelpPane.class);
      bind(Edit.Display.class).to(EditView.class);
      bind(GitPresenter.Display.class).to(GitPane.class);
      bind(SVNPresenter.Display.class).to(SVNPane.class);
      bind(BuildPresenter.Display.class).to(BuildPane.class);
      bind(Ignore.Display.class).to(IgnoreDialog.class);
      bind(CompilePdfOutputPresenter.Display.class).to(CompilePdfOutputPane.class);
      bind(FindOutputPresenter.Display.class).to(FindOutputPane.class);
      bindTab("Workspace", WorkspaceTab.class);
      bindTab("History", HistoryTab.class);
      bindTab("Data", DataTab.class);
      bindTab("Files", FilesTab.class);
      bindTab("Plots", PlotsTab.class);
      bindTab("Packages", PackagesTab.class);
      bindTab("Help", HelpTab.class);
      bindTab("VCS", VCSTab.class);
      bindTab("Build", BuildTab.class);
      bindTab("Compile PDF", CompilePdfOutputTab.class);
      bindTab("Find", FindOutputTab.class);

      bind(Shell.Display.class).to(ShellPane.class) ;
           
      bind(HelpSearch.Display.class).to(HelpSearchWidget.class) ;
      bind(CodeSearch.Display.class).to(CodeSearchWidget.class);

      bind(GitReviewPresenter.Display.class).to(GitReviewPanel.class);
      bind(SVNReviewPresenter.Display.class).to(SVNReviewPanel.class);
      bind(LineTablePresenter.Display.class).to(LineTableView.class);
      bind(HistoryPresenter.DisplayBuilder.class).to(
                                                    HistoryPanel.Builder.class);
      
      bind(PDFViewerPresenter.Display.class).to(PDFViewerPanel.class);
      bind(HTMLPreviewPresenter.Display.class).to(HTMLPreviewPanel.class);
      
      bind(GlobalDisplay.class)
            .to(DefaultGlobalDisplay.class)
            .in(Singleton.class) ;

      bind(ApplicationServerOperations.class).to(RemoteServer.class) ;
      bind(ChooseFileServerOperations.class).to(RemoteServer.class) ;
      bind(CodeToolsServerOperations.class).to(RemoteServer.class) ;
      bind(ConsoleServerOperations.class).to(RemoteServer.class) ;
      bind(SourceServerOperations.class).to(RemoteServer.class) ;
      bind(DataServerOperations.class).to(RemoteServer.class);
      bind(FilesServerOperations.class).to(RemoteServer.class) ;
      bind(HistoryServerOperations.class).to(RemoteServer.class) ;
      bind(WorkspaceServerOperations.class).to(RemoteServer.class) ;
      bind(PlotsServerOperations.class).to(RemoteServer.class) ;
      bind(PackagesServerOperations.class).to(RemoteServer.class) ;
      bind(HelpServerOperations.class).to(RemoteServer.class) ;
      bind(EditServerOperations.class).to(RemoteServer.class) ;
      bind(MirrorsServerOperations.class).to(RemoteServer.class);
      bind(VCSServerOperations.class).to(RemoteServer.class);
      bind(GitServerOperations.class).to(RemoteServer.class);
      bind(SVNServerOperations.class).to(RemoteServer.class);
      bind(PrefsServerOperations.class).to(RemoteServer.class);
      bind(ProjectsServerOperations.class).to(RemoteServer.class);
      bind(CodeSearchServerOperations.class).to(RemoteServer.class);
      bind(WorkbenchListsServerOperations.class).to(RemoteServer.class);
      bind(CryptoServerOperations.class).to(RemoteServer.class);
      bind(TexServerOperations.class).to(RemoteServer.class);
      bind(SpellingServerOperations.class).to(RemoteServer.class);
      bind(CompilePdfServerOperations.class).to(RemoteServer.class);
      bind(FindInFilesServerOperations.class).to(RemoteServer.class);
      bind(SynctexServerOperations.class).to(RemoteServer.class);
      bind(HTMLPreviewServerOperations.class).to(RemoteServer.class);
      bind(RPubsServerOperations.class).to(RemoteServer.class);
      bind(BuildServerOperations.class).to(RemoteServer.class);

      bind(WorkbenchMainView.class).to(WorkbenchScreen.class) ;

      bind(DocDisplay.class).to(AceEditor.class);
   }

   private <T extends WorkbenchTab> void bindTab(String name, Class<T> clazz)
   {
      bind(WorkbenchTab.class).annotatedWith(Names.named(name)).to(clazz);
   }

   private <T extends Widget> void bindPane(String name, Class<T> clazz)
   {
      bind(Widget.class).annotatedWith(Names.named(name)).to(clazz);
   }
}
